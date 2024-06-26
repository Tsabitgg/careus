package com.web.careus.model.news;

import com.web.careus.enumeration.ETopic;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long topicId;

    @Enumerated(EnumType.STRING)
    private ETopic topicName;
}
