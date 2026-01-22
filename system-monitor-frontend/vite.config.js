import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { nodePolyfills } from 'vite-plugin-node-polyfills';

export default defineConfig({
  plugins: [
    react(),
    nodePolyfills(),
  ],
  resolve: {
    alias: {
      crypto: 'crypto-browserify',
      stream: 'stream-browserify',
      https: 'https-browserify',
      os: 'os-browserify/browser',
      assert: 'assert',
      url: 'url',
    },
  },
  optimizeDeps: {
    include: [
      'crypto-browserify',
      'stream-browserify',
      'assert',
      'url',
      'os-browserify',
      'https-browserify',
    ],
  },
});
