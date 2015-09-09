/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

angular.module('techGallery').controller('techListController',
    function($scope, $location, $timeout) {

      $scope.showLogin = true;

      var executeAuth = function() {
        $timeout(function() {
          jsUtils.checkAuth(successFunction);
        }, 200);
      }

      $scope.login = function() {
        executeAuth();
      }

      var successFunction = function(data) {
        getTechList();
        $scope.showLogin = false;
      }

      executeAuth();

      $scope.redirectUrl = function(techId) {
        var protocol = location.protocol + '//';
        var host = protocol + location.host;
        var path = location.pathname;
        if (path === '/') {
          path = '';
        }
        var servletRedirect = '/viewTech'
        var queryString = '?id=';
        return host + path + servletRedirect + queryString + techId;
      };

      function getTechList() {
        var protocol = location.protocol + '//';
        var host = location.host;
        var complement = '/_ah/api/';
        var rootUrl = protocol + host + complement;
        gapi.client.load('rest', 'v1', callBackLoaded, rootUrl);
        //                        $scope.techList = jsUtils.mockTechList();
      }

      function callBackLoaded() {
        gapi.client.rest.getTechnologies().execute(function(data) {
          gapi.client.rest.handleLogin().execute();
          $scope.techList = data.technologies;
          $scope.$apply();
        });
      }
    });

angular.module('techGallery').controller('techDetailsController',
    function($scope, $timeout, $modal) {

      $scope.idTechnology = jsUtils.getParameterByName('id');
      $scope.loadEndorsements = true;

      //Fill this property with the domain of your choice
      $scope.domain = "@ciandt.com";

      var alerts = jsUtils.alerts;

      var successFunction = function(data) {
        var protocol = location.protocol + '//';
        var host = location.host;
        var complement = '/_ah/api/';
        var rootUrl = protocol + host + complement;
        gapi.client.load('oauth2', 'v2', function() {
          gapi.client.oauth2.userinfo.get().execute(function(resp) {
            $scope.userEmail = resp.email;
            $scope.$apply();
          })
        });
        gapi.client.load('rest', 'v1', callBackLoaded, rootUrl);
        //                            fillTechnology(jsUtils.mockTechnology());
      }

      $timeout(function() {
        jsUtils.checkAuth(successFunction);
      }, 200);

      function callBackLoaded() {
        var idTech = $scope.idTechnology;
        var req = {
          id : idTech
        };
        gapi.client.rest.getTechnology(req).execute(function(data) {
            gapi.client.rest.getUserSkill(req).execute(function(dataSkill) {
              $scope.rate = dataSkill.value;
          	  $scope.skillLevel = returnSkillLevel(dataSkill.value);
          });
          
          fillTechnology(data);
          showEndorsementsByTech();
          $scope.$apply();
        });
      }

      function fillTechnology(technology) {
        $scope.name = technology.name;
        $scope.description = technology.description;
        $scope.recommendation = technology.recommendation;
        $scope.image = technology.image;
        $scope.website = technology.website;
      }

      $scope.closeAlert = function() {
        $scope.alert = undefined;
      }

      /*
       * 
       * Begin of the Recommend Features 
       * 
       */
      $scope.endorse = function(alertUser) {
        var req = {};
        req.endorsed = $scope.endorsed;
        req.technology = $scope.idTechnology;
        if ($scope.endorsed) {
          gapi.client.rest.addEndorsement(req).execute(function(data) {
            if(alertUser){
              var alert;
              if (data.hasOwnProperty('error')) {
                alert = alerts.failure;
                alert.msg = data.error.message;
              }else{
                alert = alerts.success;
              }
              $scope.alert = alert;
            }
            $scope.endorsed = '';
            $scope.$apply();
          });
        }
      }

      /*
       * 
       * Begin of Show Endorsement Features
       * 
       */
      $scope.showAllEndorsers = function(endorsers) {
        return (endorsers.length > 0)
      }
      
      
      function showEndorsementsByTech() {
        var idTech = $scope.idTechnology;
        var req = {
          id : idTech
        };
        gapi.client.rest.getEndorsementsByTech(req).execute(function(data) {
          $scope.showEndorsementResponse = data.result.endorsements;
          $scope.loadEndorsements = false;
          $scope.$apply();
        });
      }


      $scope.open = function(endorsers, size) {
        var modalInstance = $modal.open({
          animation : true,
          templateUrl : '/showEndorsementModal.html',
          controller : 'modalController',
          size : size,
          resolve : {
            endorsers : function() {
              return endorsers;
            }
          }
        });
      };      
      
      
      /*
       * 
       * Begin of +1 features
       * 
       */
      $scope.setClassPlusOne = function(endorsers){
        var classe = 'btn btn-primary';
        for(var i in endorsers){
          if(endorsers[i].email == $scope.userEmail){
            classe = 'btn btn-danger';
          }
        }
        return classe;
      }
      
      $scope.showPlusOne = function(email){
        if($scope.userEmail == email){
          return false;
        }
        return true;
      }
      
      $scope.generateId = function(index) {
        return 'plusOne' + index;
      }

      $scope.addEndorse = function(endorsed, id) {
        setClassElement(id);
        var completeEmail = endorsed.email;
        completeEmail = completeEmail.split('@');
        var email = completeEmail[0];
        var req = {};
        req.endorsed = email;
        req.technology = $scope.idTechnology;
        gapi.client.rest.addEndorsementPlusOne(req).execute(function(data){
          if(data.hasOwnProperty('error')){
            setClassElement(id);
          }
          callBackLoaded();
        });
      }
      
      function setClassElement(id){
        var elementClassIncrease = 'btn btn-primary';
        var elementClassDecrease = 'btn btn-danger';
        var elementClass = document.getElementById(id).className;
        if (elementClass.indexOf('btn-danger') < 0) {
          document.getElementById(id).className = elementClassDecrease;
        } else {
          document.getElementById(id).className = elementClassIncrease;
        }
      }
      
      /*
       * 
       * Begin of inform skill features
       * 
       */

      //Fill user's rate and skill in that tech
      $scope.rate = 0;
      $scope.skillLevel = undefined;

      $scope.max = 5;
      $scope.isReadonly = false;

      $scope.hoveringOver = function(value) {
        $scope.overStar = value;
        $scope.percent = 100 * (value / $scope.max);
        $scope.skillLbl = returnSkillLevel(value);
      };

      $scope.$watch('rate', function(newValue, oldValue) {
        if (newValue !== oldValue) {
          $scope.skillLevel = returnSkillLevel(newValue);
          //Make API call to save the skill
          
          var idTech = $scope.idTechnology;
          var req = {
            technology : idTech,
            value : newValue
          };
          gapi.client.rest.addSkill(req).execute(function(data) {
        	  
        	  console.log(data);
          });
          
        }
      })

      function returnSkillLevel(rate) {
        switch (rate) {
        case 1:
          return 'Newbie';
          break;
        case 2:
          return 'Initiate';
          break;
        case 3:
          return 'Padawan';
          break;
        case 4:
          return 'Knight';
          break;
        case 5:
          return 'Jedi';
          break;
        default:
          return null;
        }
      }
    });

angular.module('techGallery').controller('modalController',
    function($scope, $modalInstance, endorsers) {

      $scope.endorsers = endorsers;

      $scope.ok = function() {
        $modalInstance.close();
      };

    });
