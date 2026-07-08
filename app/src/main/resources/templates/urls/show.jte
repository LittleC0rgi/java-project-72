@import hexlet.code.dto.UrlPage
@import hexlet.code.util.NamedRoutes
@import hexlet.code.util.StringUtils
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <h1 class="h1 mt-5">Сайт ${page.getUrl().getName()}</h1>

    <table class="table table-bordered table-striped table-hover" data-test="url">
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

    <h2 class="h2 mt-5">Проверки</h2>

    <form method="post" action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}">
        <input class="btn btn-primary btn-lg mb-3 px-3" type="submit" value="Запустить проверку">
    </form>

    <table class="table table-bordered table-striped table-hover" data-test="checks">
        <thead>
        <tr>
            <th>ID</th>
            <th>Код ответа</th>
            <th>h1</th>
            <th>title</th>
            <th>description</th>
            <th>Дата создания</th>
        </tr>
        </thead>
        <tbody>
        @if (page.getChecks() != null && !page.getChecks().isEmpty())
            @for (var check : page.getChecks())
                <tr>
                    <td>${check.getId()}</td>
                    <td>
                     <span class="badge
                        ${check.getStatusCode() >= 200 && check.getStatusCode() < 300 ? "bg-success" :
                          check.getStatusCode() >= 300 && check.getStatusCode() < 400 ? "bg-warning" :
                          "bg-secondary"}">
                        ${check.getStatusCode()}
                    </span>
                    </td>
                    <td>${StringUtils.truncate(check.getH1())}</td>
                    <td>${StringUtils.truncate(check.getTitle())}</td>
                    <td>${StringUtils.truncate(check.getDescription())}</td>
                    <td>${check.getCreatedAt().toLocalDate().toString()}</td>
                </tr>
            @endfor
        @else
            <tr>
                <td colspan="6">Проверок пока нет</td>
            </tr>
        @endif
        </tbody>
    </table>
`
)
