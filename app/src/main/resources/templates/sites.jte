@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.NamedRoutes
@param UrlsPage page  

@template.layout.page(content = @`
    <table data-test="urls">
        <thead>
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Дата создания</th>
            <th>Код ответа</th>
        </tr>
        </thead>
        <tbody>
        @for(var url : page.getUrls())
            <tr>
                <td>${url.getId()}</td>
                <td><a href="${NamedRoutes.urlPath(url.getId())}">https://example.com</a></td>
                <td>${url.getCreatedAt()}</td>
                <td>200</td>
            </tr>
        @endfor
        </tbody>
    </table>
`)
