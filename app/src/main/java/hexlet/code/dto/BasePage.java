package hexlet.code.dto;

import hexlet.code.types.FlashType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasePage {
    private String flash;
    private FlashType flashType;
}
