export const environment = {
  production: false,
  javaServiceEndpoint: "wallet/",
  ourAddress : "accXpfvxnZa8txuxpjyPqzBaqYPHqYu2rwn34lL8rjI=",
  ourKey : "e+dQmxnWU+X9pTWLZI6GsCXQ1QH23+rRRGZOzUkM3k1pxxel+/Gdlry3G7GmPI+rMFqpg8epi7avCffiUvyuMg==",
  monitorUrlPart : "Account?id=",
  networks: [
    {name: "Production net", serviceAddress: "https://193.124.65.121:8443", isProd: true, monitorAddress: "https://monitor.credits.com"},
    {name: "Test net", serviceAddress: "https://193.124.57.191:8443", isProd: false, monitorAddress: "https://monitor.credits.com/test"}
  ]
};
