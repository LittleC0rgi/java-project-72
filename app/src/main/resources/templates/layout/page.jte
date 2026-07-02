@import hexlet.code.dto.BasePage
@import gg.jte.Content
@import hexlet.code.util.NamedRoutes
@param BasePage page = null
@param Content content

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Hexlet Javalin Example</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
</head>
<body>
<header class="mb-2 d-flex align-items-center gap-4 bg-dark px-4 py-3">
    <a href="${NamedRoutes.basePath()}" class="h4 mb-0 text-white text-decoration-none">
        Анализатор страниц
    </a>
    <a href="${NamedRoutes.urlsPath()}" class="mb-0 text-white text-decoration-none">
        Сайты
    </a>
</header>
<main class="container">
    @if(page != null && page.getFlash() != null)
        <div class="alert alert-info mb-3" role="alert">
            ${page.getFlash()}
        </div>
    @endif
    ${content}
</main>
</body>
</html>
