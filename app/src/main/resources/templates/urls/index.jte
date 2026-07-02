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
        @for(var url : page.getUrls())
            <tr>
                <td>${url.getId()}</td>
                <td>
                    <a href="${NamedRoutes.urlPath(url.getId())}" class="link-primary text-decoration-none">
                        ${url.getName()}
                    </a>
                </td>
                <td>${url.getCreatedAt().toLocalDate().toString()}</td>
                <td><span class="badge bg-success">200</span></td>
            </tr>
        @endfor
        </tbody>
    </table>
`)
