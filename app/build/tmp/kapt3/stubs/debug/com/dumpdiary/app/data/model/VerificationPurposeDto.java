package com.dumpdiary.app.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import kotlinx.serialization.SerialName;
import kotlinx.serialization.Serializable;

@kotlinx.serialization.Serializable()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0087\u0081\u0002\u0018\u0000 \u00052\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0005B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0006"}, d2 = {"Lcom/dumpdiary/app/data/model/VerificationPurposeDto;", "", "(Ljava/lang/String;I)V", "REGISTER", "RESET_PASSWORD", "Companion", "app_debug"})
public enum VerificationPurposeDto {
    @kotlinx.serialization.SerialName(value = "REGISTER")
    /*public static final*/ REGISTER /* = new REGISTER() */,
    @kotlinx.serialization.SerialName(value = "RESET_PASSWORD")
    /*public static final*/ RESET_PASSWORD /* = new RESET_PASSWORD() */;
    @org.jetbrains.annotations.NotNull()
    public static final com.dumpdiary.app.data.model.VerificationPurposeDto.Companion Companion = null;
    
    VerificationPurposeDto() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dumpdiary.app.data.model.VerificationPurposeDto> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00c6\u0001\u00a8\u0006\u0006"}, d2 = {"Lcom/dumpdiary/app/data/model/VerificationPurposeDto$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/dumpdiary/app/data/model/VerificationPurposeDto;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.KSerializer<com.dumpdiary.app.data.model.VerificationPurposeDto> serializer() {
            return null;
        }
    }
}