@import hexlet.code.dto.UrlPage
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <table class="table table-bordered table-striped table-hover w-50" data-test="url">
        <tbody>
        <tr>
            <th scope="row" class="w-25">ID</th>
            <td>${page.getUrl().getId()}</td>
        </tr>
        <tr>
            <th scope="row">Имя</th>
            <td>${page.getUrl().getName()}</td>
        </tr>
        <tr>
            <th scope="row">Дата создания</th>
            <td>${page.getUrl().getCreatedAt().toLocalDate().toString()}</td>
        </tr>
        </tbody>
    </table>
`)
