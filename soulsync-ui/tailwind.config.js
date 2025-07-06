/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html','./src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      colors: {
        cyber: {
          50: '#e0f7ff',
          100: '#b3ecff',
          200: '#80e1ff',
          300: '#4dd6ff',
          400: '#26ccff',
          500: '#00c2ff', // main accent
          600: '#00b3e6',
          700: '#0099bf',
          800: '#007f99',
          900: '#005c66',
        }
      }
    }
  },
  plugins: [],
}
