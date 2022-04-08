package tk.pokatomnik.scpfoundation.page

const val ERROR_HTML = """
    <!doctype html>
    <html lang="ru">
        <head>
            <title>Oops!</title>
            <style>
                html, body {
                    margin: 0;
                    padding: 0;
                }
                .error-container {
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    padding: 40px;
                    text-align: center;
                }
            </style>
        </head>
        <body>
            <div class="error-container">
                <h3>Ошибка загрузки, проверьте подключение к интернету</h3>
            </div>
        </body>
    </html>
"""