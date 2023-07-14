module.exports = {
  productionSourceMap: false,
  devServer: {
    proxy: {
      '/api/': {
        target: 'http://localhost:8001'
      }
    },
    port: 8080
  }
};