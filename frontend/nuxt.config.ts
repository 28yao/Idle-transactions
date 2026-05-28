export default defineNuxtConfig({
  compatibilityDate: '2024-11-01',
  devtools: { enabled: true },

  modules: [
    '@element-plus/nuxt',
    '@pinia/nuxt',
  ],

  css: [
    '~/assets/css/main.css',
  ],

  runtimeConfig: {
    public: {
      apiBase: process.env.API_BASE || 'http://localhost:8080',
    },
  },

  app: {
    head: {
      title: '校园二手平台',
      meta: [
        { charset: 'utf-8' },
        { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      ],
    },
  },
})
