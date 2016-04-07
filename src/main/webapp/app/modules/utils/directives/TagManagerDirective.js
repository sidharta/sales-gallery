module.exports = function() {
  return {
    restrict: 'E',
    scope: {
      tags: '=',
      autocomplete: '=autocomplete',
      disable: '='
    },
    template:
    '<div class="tags">' +
    '<div ng-repeat="(idx, tag) in tags" class="tag label technology-chips">{{tag}} <button class="bnt-chip" ng-disabled={{disable}} ng-click="remove(idx)">x</button>  </div>' +
    '</div>' +
    '<div class="input-group"><input type="text" class="form-control" placeholder="Adicione alguma" ng-model="newValue" />' +
    '</div>' ,

    link: function ( $scope, $element, $attr ) {
      var $ = require('jquery');
      require('jquery-ui/autocomplete');

      var input = angular.element($element).find('input');

      // setup autocomplete
      if ($scope.autocomplete) {
        $scope.autocompleteFocus = function(event, ui) {
          input.val(ui.item.value);
          return false;
        };
        $scope.autocompleteSelect = function(event, ui) {
          $scope.newValue = ui.item.value;
          $scope.$apply( $scope.add );

          return false;
        };
        $($element).find('input').autocomplete({
          minLength: 0,
          create: function (e) {
              $(this).prev('.ui-helper-hidden-accessible').remove();
          },
          source: function(request, response) {
            var item;
            return response((function() {
              var _i, _len, _ref, _results;
              _ref = $scope.autocomplete;
              _results = [];
              for (_i = 0, _len = _ref.length; _i < _len; _i++) {
                item = _ref[_i];
                if (item.toLowerCase().indexOf(request.term.toLowerCase()) !== -1) {
                  _results.push(item);
                }
              }
              return _results;
            })());
          },
          focus: (function(_this) {
            return function(event, ui) {
              return $scope.autocompleteFocus(event, ui);
            };
          })(this),
          select: (function(_this) {
            return function(event, ui) {
              return $scope.autocompleteSelect(event, ui);
            };
          })(this)
        });
      }


      // adds the new tag to the array
      $scope.add = function() {
        // if not dupe, add it
        if(!$scope.tags) {
          $scope.tags = [];
        }

        if ($scope.tags.indexOf($scope.newValue)==-1){
          $scope.tags.push( $scope.newValue );
        }
        $scope.newValue = "";
      };

      // remove an item
      $scope.remove = function ( idx ) {
        $scope.tags.splice( idx, 1 );
      };

      // capture keypresses
      input.bind( 'keypress', function ( event ) {
        // enter was pressed
        if ( event.keyCode == 13 ) {
          $scope.$apply( $scope.add );
        }
      });
    }
  };
}
