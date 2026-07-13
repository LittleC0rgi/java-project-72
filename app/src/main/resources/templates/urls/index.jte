@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes
@import io.javalin.http.HttpStatus
@param UrlsPage page  

@template.layout.page(
page = page,
content = @`
    <table class="table table-striped table-hover table-bordered align-middle" data-test="urls">
        <thead class="table-light">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Имя</th>
            <th scope="col">Последняя проверка</th>
            <th scope="col">Код ответа</th>
        </tr>
        </thead>

        <tbody>
        @for(var urlInfo : page.getUrls())
            <tr>
                <td>${urlInfo.getUrl().getId()}</td>
                <td>
                    <a href="${NamedRoutes.urlPath(urlInfo.getUrl().getId())}"
                       class="link-primary text-decoration-none">
                        ${urlInfo.getUrl().getName()}
                    </a>
                </td>
                <td>
                    @if(urlInfo.getLastCheck() != null)
                        ${urlInfo.getLastCheck().getCreatedAt().toLocalDate().toString()}
                    @else
                        —
                    @endif
                </td>
                <td>
                    @if(urlInfo.getLastCheck() != null)
                        <span class="badge
                            ${urlInfo.getLastCheck().getStatusCode() >= HttpStatus.OK.getCode() && urlInfo.getLastCheck().getStatusCode() < HttpStatus.MULTIPLE_CHOICES.getCode() ? "bg-success" :
                              urlInfo.getLastCheck().getStatusCode() >= HttpStatus.MULTIPLE_CHOICES.getCode() && urlInfo.getLastCheck().getStatusCode() < HttpStatus.BAD_REQUEST.getCode() ? "bg-warning" :
                              "bg-secondary"}">

                            ${urlInfo.getLastCheck().getStatusCode()}
                        </span>
                    @else
                        <span class="badge bg-secondary">
                            Нет проверок
                        </span>
                    @endif
                </td>
            </tr>
        @endfor
        </tbody>
    </table>
`)
