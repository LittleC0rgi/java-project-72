@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes
@param UrlsPage page  

@template.layout.page(
page = page,
content = @`
    <table class="table table-striped table-hover table-bordered align-middle" data-test="urls">
        <thead class="table-light">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Имя</th>
            <th scope="col">Дата создания</th>
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
                <td>${urlInfo.getUrl().getCreatedAt().toLocalDate().toString()}</td>
                <td>
                    @if(urlInfo.getLastCheck() != null)
                        <span class="badge
                            ${urlInfo.getLastCheck().getStatusCode() >= 200 && urlInfo.getLastCheck().getStatusCode() < 300 ? "bg-success" :
                              urlInfo.getLastCheck().getStatusCode() >= 300 && urlInfo.getLastCheck().getStatusCode() < 400 ? "bg-warning" :
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
