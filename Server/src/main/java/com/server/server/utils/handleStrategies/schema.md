| Handler Class   | Method   | Expected Packet Format                                  |
|-----------------|----------|---------------------------------------------------------|
| RegisterHandler | register | Packet ("register", username, "client")                 |
| LoginHandler    | login    | Packet ("login", username, "client")                    |
| EmailHandler    | email    | Packet ("email", Email object, "client")                |
| DeleteHandler   | delete   | Packet ("delete", id, "client")                         |
| RefreshHandler  | refresh  | Packet ("refresh", List of present email ids, "client") |