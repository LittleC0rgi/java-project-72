package hexlet.code.dto;

import hexlet.code.model.Url;
import lombok.Getter;

@Getter
public class UrlPage extends BasePage {
    private Url url;
}
