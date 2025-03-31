package com.ctg.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;


public enum UserQuestTransactionStatus {
    IN_PROGRESS,
    COMPLETED
}
