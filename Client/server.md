# Server

| Request | Response | Success Payload          | Failure Payload                      |
| --- | --- |--------------------------|--------------------------------------|
|'register'| 'succesful'/ 'failed'| null                     | String(Reason of failure)            |
|'login'| 'succesful'/ 'failed'| null                     | String(Reason of failure)            |
|'send'| 'succesful'/ 'failed'| null                     | String(Reason of failure)            |
|'refresh'| 'succesful'/ 'failed'| `List<Email> = DiffEmails` | `List <Email> = Empty` |
|'delete' | 'succesful'/ 'failed'| null                     | String(Reason of failure)            |