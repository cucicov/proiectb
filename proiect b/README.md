- to get current content, two calls are needed
  - /recentcontent/metadata returns the metadata of the latest content and a public token
  - /content/{token} public token is used to query again for the data content. headers will be set according to the content type.

