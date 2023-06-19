package com.example.sendmessage.enums;

public enum TypeTemplateEnum {

    SMS_OTP("SMS_OTP", "TEMPLATE SMS OTP"),
    SMS_RECOVERY_PASSWORD("SMS_RECOVERY_PASSWORD", "TEMPLATE SMS RECOVERY PASSWORD"),
    EMAIL_OTP("EMAIL_OTP", "TEMPLATE EMAIL OTP"),
    EMAIL_RECOVERY_PASSWORD("EMAIL_RECOVERY_PASSWORD", "TEMPLATE EMAIL RECOVERY PASSWORD"),
    Email_Welcome("Email_Welcome", "TEMPLATE EMAIL WELCOME");

    private String identifier;

    private String description;

    /** Contrutor Padrao */
    TypeTemplateEnum(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    /** GET */
    public String getIdentifier() {
        return identifier;
    }

    /** GET */

    public String getDescription() {
        return description;
    }

    // Get by identifier
    public static TypeTemplateEnum getByIdentifier(String identifier) {
        for (TypeTemplateEnum templateInfoEnum : TypeTemplateEnum.values()) {
            if (templateInfoEnum.getIdentifier().equalsIgnoreCase(identifier)) {
                return templateInfoEnum;
            }
        }
        return null;
    }
}
