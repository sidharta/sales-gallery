module.exports = function ($scope, $uibModalInstance, image) {
  $scope.image = image;
  $scope.resultImage = 'assets/images/no_image.png';
  
  $scope.ok = function() {
    $uibModalInstance.close($scope.resultImage);
  };
  
  $scope.cancel = function() {
    $uibModalInstance.dismiss('cancel');
  }; 
};
