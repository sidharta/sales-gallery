module.exports = function ($rootScope, AppService, TechnologyService, $stateParams, $state, $scope) {

  /**
   * Object context
   * @type {Object}
   */
  var context = this;

  /**
   * Loading state
   * @type {Boolean}
   */
  this.loading = false;
  context.backgroundColor = '#FFF';

  context.technologies =[];
  context.allTechnologies = [];


  this.regexPipedrive = "^(http(s)?:\\/\\/(\\w+.pipedrive.com\\/deal\\/)*\\d+)";
  this.regexGoogledrive = "(http(s)?:\\/\\/(drive.google.com\\/open\\?id=)\\S+)"


  this.login = function(){
    checkLogin(false);
  };

  AppService.setPageTitle('Adicionar nova proposta');

  TechnologyService.getTechnology($stateParams.id).then(function(data){
    if (data.hasOwnProperty('error')) {
      context.showContent = false;
      context.showTechNotExists = true;
      return;
    }
    AppService.setPageTitle('Editar proposta');
    fillTechnology(data);
  });

  TechnologyService.getStatus().then(function(data){
    context.dropDownStatus = data;
  });

  TechnologyService.getTowers().then(function(data){
    context.dropDownTowers = data;
  });

  TechnologyService.getOffers().then(function(data){
    context.dropDownOffer = data;
  });

  TechnologyService.findTechnologyName().then(function(data){
    context.allTechnologies = data.items;
  })

  this.addOrUpdateTechnology = function(form){
    var isEdit = (context.id !== undefined);
    if(context.name != null && context.description != null && context.shortDescription != null) {
      TechnologyService.addOrUpdate(context).then(function(data){
        if (data.hasOwnProperty('error')) {
          AppService.setAlert(data.error.message, 'error');
        }else{
          if(context.addNew){
            clearTechnology();
            form.$setPristine();
            form.$setUntouched();
          }else {
            $rootScope.technologySaved = true;
            $state.go('root.technologies');
          }
          if(isEdit){
            AppService.setAlert('Proposta editada com sucesso', 'success');
          }else{
            AppService.setAlert('Proposta criada com sucesso', 'success');
          }
        }
      });
    }
  };

  /*
   * Function to clean the technology informations
   */
  function clearTechnology() {
    context.name = '';
    context.description = '';
    context.shortDescription = '';
    context.webSite = '';
    context.client = '';
    context.ownerEmail = '';
    context.ownerName = '';
    context.technologies = '';
    context.pipedriveLink = '';
    document.getElementById('technology-name').value = null;
    document.getElementById('list').innerHTML = ['<img src="/assets/images/no_image.png" title="Insira uma imagem" />'].join('');
  }

  function fillTechnology(technology) {
    context.name = technology.name;
    context.id = technology.id;
    context.shortDescription = technology.shortDescription;
    context.description = technology.description;
    context.client = technology.client;
    context.technologies = technology.technologies;
    context.pipedriveLink = technology.pipedriveLink;
    context.webSite = technology.website;
    context.ownerEmail = technology.ownerEmail;
    context.ownerName = technology.ownerName;
    context.image = technology.image;
    if(context.image){
      document.getElementById('list').innerHTML = ['<img src="', context.image,'" title="', context.name, '" />'].join('');
    }
    context.selectedStatus = technology.status;
    context.selectedTower = technology.tower;
    context.offers = technology.offers;
    context.creationDate = technology.creationDate;
    context.pipedriveLink = technology.pipedriveLink;
    context.hasPipedriveLink = !!context.pipedriveLink;
  }

  this.selectStatus = function(selected){
    context.selectedStatus = selected;
  };

  this.selectTower = function(selected){
    context.selectedTower = selected;
  }

  $scope.handleFileSelect = function(file) {
      var files = file.files;
      var f = files[0];
      if (f) {
        var reader = new FileReader();
        reader.onload = (function(theFile) {
          return function(e) {
            var img = new Image;
            img.src = reader.result;
            img.onload = function() {
              if(f.type != 'image/png' || img.width > 100 || img.height > 100){
                alert('Esta imagem tem um tamanho ou tipo errado, escolha uma imagem com o tamanho 100x100 e tipo PNG.');
                document.getElementById('technology-image').value = null;
                document.getElementById('list').innerHTML = ['<img src="/assets/images/no_image.png" title="Insira uma imagem" width="100" />'].join('');
              }else{
                var image = e.target.result;
                context.image = image.replace('data:image/png;base64,', '');
                document.getElementById('list').innerHTML = ['<img src="', e.target.result,'" title="', theFile.name, '" width="100" />'].join('');
              }
            };
          };
        })(f);
        reader.readAsDataURL(f);
      }
      else {
        document.getElementById('list').innerHTML = ['<img src="/assets/images/no_image.png" title="Insira uma imagem" width="100" />'].join('');
      };
  }

  function setClassElement(id){
    var elementClassIncrease = 'btn GPlusDefault';
      var elementClassDecrease = 'btn GPlusAdded';
      var elementClass = document.getElementById(id).className;
      if (elementClass.indexOf('GPlusAdded') < 0) {
        document.getElementById(id).className = elementClassDecrease;
      } else {
        document.getElementById(id).className = elementClassIncrease;
      }
  }

  this.onLostFocus = function(link){
    var self = this;

    if (link == undefined || link == '') {
      self.clearPipedrive();
      return;
    }

    var s = link.split('/');
    var id = s[4];

    TechnologyService.getPipedriveDeal(id).then(function(data){
      if( data.name ) {
        context.backgroundColor = '#EEE';
        context.name = data.name;
        context.selectedStatus  = data.status;
        context.selectedTower = data.tower;
        context.offers = data.offers;
        context.client = data.client;
        context.ownerEmail = data.ownerEmail;
        context.ownerName = data.ownerName
      } else if (data.error){
        AppService.setAlert(data.message, 'error');
        self.clearPipedrive();
      } else {
        self.clearPipedrive();
      }
    });

  }
  this.clearPipedrive = function() {
    context.backgroundColor = '#FFF';
    context.name = '';
    context.client = '';
    context.ownerEmail = '';
    context.ownerName = '';
    context.pipedriveLink = '';
    context.selectedStatus = null;
    context.selectedTower = null;
    context.offers = null;
  };
}
