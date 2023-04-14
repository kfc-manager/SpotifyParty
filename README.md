# SpotifyParty

This is a Spring Boot Application build on top of the SpotifyAPI. You can connect your Spotify account to it and it will provide endpoints to search for songs and add them to the Queue, which makes it perfect for having friends over and everybody being able to add songs they want to be played!

## Endpoints

**/login**

* with this endpoint an access token is generated, so the SpotifyAPI can be used

* needs to be accessed and the application won't work without it

* localhost:8080/login

**/search**

* with this endpoint you can search for a song with a provided query

* example: localhost:8080/search?q=loseyourself

* this endpoint will provide the top 20 search results related to the query


**/queue/add**

* with this endpoint a song can be added to the queue, the song id must be provided

* example: localhost:8080/queue/add?track=7MJQ9Nfxzh8LPZ9e9u68Fq

**/queue/get**

* this endpoint will return the song that is currently playing and the first 20 songs of the queue

* localhost:8080/queue/get


## Connecting Spotify account

To connect your account you need to [create an app](https://developer.spotify.com/dashboard/create) at the spotify dashboard and enter as Redirect URI "ht<span>tp://</span>localhost:8080/callback". Then you need to copy your client id and client secret into the String constants "CLIENT_ID" and "CLIENT_SECRET" in [this](https://github.com/kfc-manager/SpotifyParty/blob/main/src/main/java/com/kalle/spotifyparty/spotifyapi/SpotifyAPI.java).

