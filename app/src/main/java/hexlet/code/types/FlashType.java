package hexlet.code.types;

import lombok.Getter;

@Getter
public enum FlashType {
    SUCCESS("success"),
    INFO("info"),
    WARNING("warning"),
    DANGER("danger");

    private final String bootstrapClass;

    FlashType(String bootstrapClass) {
        this.bootstrapClass = bootstrapClass;
    }
}
