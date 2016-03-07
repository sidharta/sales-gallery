angular
  .module('TechGallery.Utils', [])
  .service('Analytics', require('./services/Analytics'))
  .filter('nl2p', require('./filters/nl2pFilter'))
  .filter('range', require('./filters/rangeFilter'))
  .directive('loading', require('./directives/LoadingDirective'))
  .directive('placeholderImg', require('./directives/PlaceholderImgDirective'))

module.exports = 'TechGallery.Utils';
