package com.oocode

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

val app: HttpHandler = routes(
    "/" bind GET to {
        val queries = it.queries("q")
        Response(OK).body(queries.firstOrNull()?.let { question ->
            println("question = ${question}")
            Answerer().answerFor(question) } ?: HomePage.HTML)
    }
)

fun main() {
    val server = SimpleHttp4kServer.http4kServer.start()
    println("Server started on " + server.port())
}

object SimpleHttp4kServer {
    val http4kServer = app.asServer(SunHttp(8124))
}

private object HomePage {
    val HTML = """
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Simple Web Service</title>
    <script src="https://unpkg.com/htmx.org@2.0.4"></script>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            background-color: #f7f7f7;
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            flex-direction: column;
        }
        .container {
            text-align: center;
            max-width: 600px;
            padding: 30px;
            background-color: #fff;
            border-radius: 20px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
        }
        h1 {
            font-size: 2.5em;
            font-weight: 600;
            color: #333;
            margin-bottom: 0.5em;
        }
        p {
            font-size: 1.1em;
            color: #777;
            margin-bottom: 1.5em;
        }
        ul {
            list-style: none;
            padding-left: 0;
            color: #555;
            margin-bottom: 1.5em;
        }
        ul li {
            margin-bottom: 10px;
            font-size: 1.2em;
        }
        .search-box {
            width: 70%;
            padding: 12px 20px;
            border-radius: 25px;
            border: 1px solid #ddd;
            font-size: 1.2em;
            outline: none;
            transition: all 0.3s ease;
            margin-bottom: 20px;
        }
        .search-box:focus {
            border-color: #007bff;
            box-shadow: 0 0 10px rgba(0, 123, 255, 0.25);
        }
        .search-results {
            font-size: 1.2em;
            color: #333;
            margin-top: 10px;
        }
        .result-container {
            background-color: #f9f9f9;
            padding: 10px 15px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            display: inline-block;
            margin-top: 20px;
        }
        .result-container span {
            font-weight: bold;
            color: #007bff;
        }
        .footer {
            font-size: 0.9em;
            color: #bbb;
            position: absolute;
            bottom: 20px;
        }
        .footer a {
            text-decoration: none;
            color: #007bff;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to the Simple Web Service</h1>
        <p>This minimalist web app allows you to ask questions and receive answers instantly.</p>

        <ul>
            <li>What is your forename?</li>
            <li>What is 2 + 2?</li>
        </ul>
        
        <input class="search-box" 
               type="search"
               name="q" 
               placeholder="Ask a question ..."
               hx-get="/"
               hx-params="*"
               hx-trigger="input changed delay:500ms, keyup[key=='Enter'], load"
               hx-target="#search-results">

        <div id="search-results" class="search-results">
            <div id="result-container"></div>
        </div>
    </div>

    <div class="footer">
        <p>Powered by <a href="https://www.http4k.org/" target="_blank">http4k</a> | Simple, Fast, Lightweight</p>
    </div>

    <script>
        // Add some interactivity: update the search results div dynamically
        document.querySelector('input[name="q"]').addEventListener('input', function() {
            const query = this.value.trim();
            const resultContainer = document.getElementById('result-container');
            if (query.length > 0) {
                resultContainer.innerHTML = `Searching for: <span id="search-results"/>`;
            } else {
                resultContainer.innerHTML = '';
            }
        });
    </script>
</body>
</html>
""".trim()
}
