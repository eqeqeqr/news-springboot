package org.event.pojo.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDao {
    @Value("${spring.mail.username}")
    String senderEmail;
    @Value("${spring.mail.username}")
    String sender;
}

