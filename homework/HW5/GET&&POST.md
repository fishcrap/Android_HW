# difference bewtween GET and POST

>**GET** method requests a representation of the specified resource. Note that GET should not be used for operations that cause side-effects, such as using it for taking actions in web applications. One reason for this is that GET may be used arbitrarily by robots or crawlers,which should not need to consider the side effects that a request should cause.


>**POST** submits data to be processed (e.g., from an HTML form) to the identified resource. The data is included in the body of the request. This may result in the creation of a new resource or the updates of existing resources or both.


The above answer is from stackoverflow.

And from Wikipedia, I find the difference is obvious and clear.

>The **POST** method requests that the server accept the entity enclosed in the request as a new subordinate of the web resource identified by the URI.

>The **GET** request method retrieves information from the server.

**So essentially GET is used to retrieve remote data, and POST is used to insert/update remote data.**