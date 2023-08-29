# Concurrency Simple Chat

The ChatServer class listens on a specific port, accepting multiple client connections. Each connected client is handled by a separate thread managed by a thread pool. The server supports broadcasting messages to all connected clients and private messaging between clients.

The ChatClient class represents the client-side of the application. It connects to the server, initiates a separate thread for receiving messages, and allows the user to input messages from the console to be sent to the server. The client can also send private messages to specific recipients by using the '@' symbol followed by the recipient's name.

The ChatServerWorker class is responsible for managing individual client connections. It receives messages from clients, distinguishes between public and private messages, and broadcasts them accordingly. It handles setting client names and welcoming them to the chat. When the server sends messages to clients, it utilizes the ChatClientWorker thread running on the client's side.

Project made during the Academia de Código bootcamp between May -> Aug 2023. www.academiadecodigo.org
<p></p>
<p></p>
Linux and macOS

nc -p <source_port> localhost 8000
<p></p>
<p></p>
Windows (https://nmap.org/)

ncat -p <source_port> localhost 8000

<source_port>: The port number you want to use as the source port for the connection.


