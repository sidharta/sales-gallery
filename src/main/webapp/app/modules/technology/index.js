angular
  .module('TechGallery.Technology', [require('ng-img-crop-alias')])
  .service('TechnologyService', require('./services/TechnologyService'))
  .controller('TechnologiesController', require('./controllers/TechnologiesController'))
  .controller('TechnologyController', require('./controllers/TechnologyController'))
  .controller('TechnologyAddController', require('./controllers/TechnologyAddController'))
  .controller('SearchController', require('./controllers/SearchController'))
  .controller('CropModalController', require('./controllers/CropModalController'))
  .config(require('./routes'));

module.exports = 'TechGallery.Technology';
