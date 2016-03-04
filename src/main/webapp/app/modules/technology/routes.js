module.exports = function(
  $stateProvider,
  $urlRouterProvider
) {

  /**
   * Views folder for the controller
   * @type {String}
   */
  var viewsFolder = 'app/modules/technology/views/';

  $urlRouterProvider
    .when('', '/proposals')
    .when('/', '/proposals');

  $stateProvider
    .state('root.technologies', {
      url: '/proposals',
      controller: 'TechnologiesController as technologies',
      templateUrl: viewsFolder + 'technologies.html'
    })
    .state('root.technologies-add', {
      url: '/proposals/new',
      controller: 'TechnologyAddController as technology',
      templateUrl: viewsFolder + 'technology-add.html'
    })
    .state('root.technologies-view', {
      url: '/proposals/:id',
      controller: 'TechnologyController as technology',
      templateUrl: viewsFolder + 'technology.html'
    })
    .state('root.technologies-edit', {
      url: '/proposals/:id/edit',
      controller: 'TechnologyAddController as technology',
      templateUrl: viewsFolder + 'technology-add.html'
    })
};
