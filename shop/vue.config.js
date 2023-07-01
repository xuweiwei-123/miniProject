const { defineConfig } = require('@vue/cli-service')
module.exports = {
  devServer: {
    proxy: {
      '/supermarket': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/supermarket': '/supermarket'
        }
      }
    }
  },
  transpileDependencies: [
    'vue'
  ]
}

