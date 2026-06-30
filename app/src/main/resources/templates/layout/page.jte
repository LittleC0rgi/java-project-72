@import hexlet.code.dto.BasePage
@import gg.jte.Content
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
    <h2 class="h4 mb-0 text-white">Анализатор страниц</h2>
    <p class="mb-0 text-gray text-white-50">Сайты</p>
</header>
<main class="container">
    @if(page != null && page.getFlash() != null)
        <p>${page.getFlash()}</p>
    @endif
    ${content}
</main>
</body>
</html>
