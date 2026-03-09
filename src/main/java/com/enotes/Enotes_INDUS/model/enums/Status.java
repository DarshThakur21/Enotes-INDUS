package com.enotes.Enotes_INDUS.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)  // ← ADD
public enum Status {
    PENDING,
    COMPLETED,
    IN_PROGRESS,
}
