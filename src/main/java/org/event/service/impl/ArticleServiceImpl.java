package org.event.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.event.mapper.ArticleMapper;
import org.event.mapper.RoleMapper;
import org.event.mapper.UserRoleMapper;
import org.event.pojo.Article;
import org.event.pojo.PageBean;
import org.event.pojo.Role;
import org.event.pojo.UserRole;
import org.event.pojo.dao.ArticleDao;
import org.event.pojo.dao.ArticleStatisticsDao;
import org.event.service.ArticleService;
import org.event.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        Map<String, Object> map = ThreadLocalUtil.get();
        article.setCreateUser((Integer) map.get("id"));
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //1.创建pageBean
        PageBean<Article> pageBean = new PageBean<>();
        System.out.println("pageMum" + pageNum + "pageSize" + pageSize+"state" + state+"category"+categoryId);

        //3.调用mapper

        //page中提供方法，可以获取PageHelper分页查询后的得到的总记录数，和当前页数据
        UserRole userRole = userRoleMapper.getByUserId(ThreadLocalUtil.getUserId());
        if (userRole == null) {
            throw new RuntimeException("用户未分配角色");
        }
        Role role = roleMapper.getById(userRole.getRoleId());
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        Integer weight = role.getWeight();
        //2.开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<Article> page = articleMapper.list(ThreadLocalUtil.getUserId(), categoryId, state, weight, null);
        List<Article> articleList = page.getResult();
        Long total = page.getTotal();
        //填充数据到PageBean
        pageBean.setItems(articleList);
        pageBean.setTotal(total);
        return pageBean;
    }

    @Override
    public Article detail(Integer id) {
        return articleMapper.detail(id);
    }

    @Override
    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    @Override
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }

    @Override
    public Map<String, List> statistics(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, List> map = new HashMap<>();


        startTime = startTime.withHour(0).withMinute(0).withSecond(0);
        endTime = endTime.withHour(23).withMinute(59).withSecond(59);
        System.out.println(startTime + "ss" + endTime);
        List<ArticleStatisticsDao> articleList = articleMapper.articleCount(startTime, endTime);
        List<String> roleNameList = new ArrayList<>();
        List<Integer> articleCountList = new ArrayList<>();
        for (ArticleStatisticsDao dao : articleList) {
            roleNameList.add(dao.getRoleName());
            articleCountList.add(dao.getArticleNumber());
        }
        map.put("roleNameList", roleNameList);
        map.put("articleCountList", articleCountList);
        return map;
    }

    @Override
    public Map<String, List> statisticsLine(LocalDateTime startTime, LocalDateTime endTime) {
        startTime = startTime.withHour(0).withMinute(0).withSecond(0);
        endTime = endTime.withHour(23).withMinute(59).withSecond(59);
        List<String> dateList = new ArrayList();
        List<Integer> articleNumberList = new ArrayList<>();
        LocalDate startDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();
        // 计算两个日期之间的总天数
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        int dayNum = 1;
        if (daysBetween > 33) {
            dayNum = 30;
        }
        while (startTime.isBefore(endTime)) {
            startTime = startTime.plusDays(dayNum);
            System.out.println(startTime + "dasdasd" + endTime);
            int number = articleMapper.articleNumber(startTime, startTime.plusDays(dayNum));

            articleNumberList.add(number);
            dateList.add(String.valueOf(startTime.toLocalDate()));
        }

        Map<String, List> map = new HashMap<>();
        map.put("dateList", dateList);
        map.put("articleNumberList", articleNumberList);
        return map;
    }

    @Override
    public void exportBusinessData(HttpServletResponse response) {
        //1.查询数据库，获取营业数据
        LocalDateTime todayTime = LocalDateTime.now();
        LocalDateTime startTime = todayTime.withHour(0).withMinute(0).withSecond(0).minusDays(30);
        LocalDateTime endTime = todayTime.withHour(23).withMinute(59).withSecond(59);
        List<ArticleDao> articleList = articleMapper.Data30List(startTime, endTime);
        //2.通过poi将数据写入到excel文件中
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"业务数据报表.xlsx\"");


        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/文章数据报表模板.xlsx");
        try {
            //用模板文件创建一个报表
            XSSFWorkbook excel = new XSSFWorkbook(in);
            //获取表格标签页
            XSSFSheet sheet = excel.getSheetAt(0);
            //模板文件行数据第几行开始
            int rowNum = 4;
            //获取响应对象的的输出流
            ServletOutputStream outputStream = response.getOutputStream();
            for (ArticleDao articleDao : articleList) {
                XSSFRow row = sheet.getRow(rowNum);
                row.getCell(1).setCellValue(String.valueOf(articleDao.getCreateTime().toLocalDate()));
                row.getCell(2).setCellValue(articleDao.getTitle());
                row.getCell(3).setCellValue(articleDao.getCategoryName());
                row.getCell(4).setCellValue(articleDao.getContent().replaceAll("<p>|</p>", ""));
                row.getCell(5).setCellValue(articleDao.getState());
                row.getCell(6).setCellValue(articleDao.getCreateUser());
                row.getCell(7).setCellValue(articleDao.getCreateUserName());
                rowNum++;
            }
            //3.通过输出流将excel文件下载到客户端
            excel.write(outputStream);
            //关闭资源
            outputStream.close();
            ;
            excel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public PageBean<Article> Indexlist(Integer pageNum, Integer pageSize, String searchContent, Integer categoryId) {
        //1.创建pageBean
        PageBean<Article> pageBean = new PageBean<>();
        System.out.println("pageMum" + pageNum + "pageSize" + pageSize+"categoryId"+categoryId+"searchContent"+searchContent);

        //3.调用mapper

        //page中提供方法，可以获取PageHelper分页查询后的得到的总记录数，和当前页数据

        //2.开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<Article> page = articleMapper.list(null, categoryId, "已发布", null, searchContent);
        List<Article> articleList = page.getResult();
        Long total = page.getTotal();
        //填充数据到PageBean
        pageBean.setItems(articleList);
        pageBean.setTotal(total);
        return pageBean;
    }
}
