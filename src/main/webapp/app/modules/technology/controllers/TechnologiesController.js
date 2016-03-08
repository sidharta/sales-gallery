module.exports = function ($scope, $rootScope, AppService, TechnologyService, UserService, $timeout, API) {

  /**
   * Object context
   * @type {Object}
   */
   var context = this;

  /**
   * Listner to update list when text filter is fired
   * @type {Array}
   */
   $scope.$on('searchChange', function(event, data) {
    context.items = data.technologies;
    context.loading = false;
    $scope.$apply();
  })

  this.split = function(string) {
    var array = string.split(',');
    return array;
  }

  /**
   * List of technologies
   * @type {Array}
   */
   this.getTechnologies = function(){
    context.loading = true;
    TechnologyService.getTechnologies().then(function(){
      context.items = TechnologyService.foundItems;
      context.loading = false;
    });
   }

   this.getTechnologies();

  /**
   * Loading state
   * @type {Boolean}
   */
   this.loading = true;

  this.statusFilter = null;
  this.orderFilter = null;
  this.lastActivityDateFilter = null;

   this.updateFilters = function(){
    context.loading = true;
    if(context.statusFilter === ''){
      context.statusFilter = null;
    }
    if(context.lastActivityDateFilter === ''){
      context.lastActivityDateFilter = null;
    }
    if(context.offerFilter === ''){
      context.offerFilter = null;
    }
    TechnologyService.setContentFilters(context.statusFilter, context.orderFilter, context.lastActivityDateFilter, context.offerFilter);
   }

  /**
   * Reset technologies filters
   * @return {Void}
   */
  this.resetFilters = function () {
    context.statusFilter = null;
    context.orderFilter = null;
    context.offerFilter = null;
    context.lastActivityDateFilter = null;
    context.updateFilters();
  }

   this.followTechnology = function(idTechnology, $event){
    context.currentElement = $event.currentTarget;
    TechnologyService.followTechnology(idTechnology).then(function(data){
      if(!data.hasOwnProperty('error')){
        changeFollowedClass(context.currentElement);
      }
    });
   }

   function changeFollowedClass(element){
    if(element.className.indexOf('btn-default') > 0){
      element.className = 'btn btn-xs btn-danger';
    }else{
      element.className = 'btn btn-xs btn-default';
    }
    context.currentElement = undefined;
  }

  this.setFollowedClass = function(isFollowedByUser){
    if(isFollowedByUser){
      return 'btn btn-xs btn-danger';
    }
    return 'btn btn-xs btn-default';
  }

  this.deleteTechnology = function(idTechnology){
    if(confirm('Você realmente quer apagar a proposta?')) {
      context.loading = true;
      TechnologyService.deleteTechnology(idTechnology).then(function(data){
        if(!data.hasOwnProperty('error')){
          context.getTechnologies();
        }
      });
    }
  }

  /**
   * Page title
   */
   AppService.setPageTitle('Propostas');

   TechnologyService.getOffers().then(function(data){
     context.offers = data;
   });

   this.getImageStatus = function(status){
      if (status === 'Proposta'){
        return 'shoeprints.png';
      }else if (status === 'Perdida'){
        return 'denied.png';
      }else if (status === 'Ganha'){
        return 'trophy-award-icon.png';
      }else{
        return 'placeholder.png';
      }
   }


 }
