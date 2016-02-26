angular
  .module('TechGallery.Auth', [])
  .service('AuthService', require('./services/AuthService'))
  .directive('login', require('./directives/LoginDirective'))
  .constant('CLIENT_ID', '283982869193-fj96oloi2qnub1uq8uovk4r75dc5gjmo.apps.googleusercontent.com')
  .constant('USER_SCOPES', 'https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.email');

module.exports = 'TechGallery.Auth';
