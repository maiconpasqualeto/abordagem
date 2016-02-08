var app = angular.module('dibo', ['ui.mask','base64','ngMessages']);

app.controller('ctrlEmail', function($scope, $http, $window, $location) {
	$scope.enviarEmail = function(email) {
		$scope.jaconfirmado = false;
		$scope.formEmail.txtEmail.$touched = true;
		
		if ($scope.formEmail.$valid) {
			$http({
				method : 'POST',
				data: email,
				url : $location.absUrl() + '/email'
			}).then(function successCallback(response) {				
				console.log("OK")				
				$window.location.href = '/enviado.html';
				
			}, function errorCallback(response) {
				if (response.status == 302) {
					$scope.jaconfirmado = true;
				} else
					console.log("Erro");
			});
		}
	};
});

app.controller('ctrlCadastro', 
		function($scope, $http, $location, $window, $filter) {
	
	$scope.jaCadastrado = false;
		
	$scope.cadastrar = function(aviador) {
		
		$scope.formCadastro.txtNome.$touched = true;
		$scope.formCadastro.txtEmail.$touched = true;
		$scope.formCadastro.txtTelefone.$touched = true;
		$scope.formCadastro.txtPrefixo.$touched = true;
		$scope.formCadastro.txtModelo.$touched = true;
		$scope.formCadastro.txtQtd.$touched = true;
		
		if ($scope.formCadastro.$valid) {
			aviador.prefixo = $filter('uppercase')(aviador.prefixo);
			console.log("posting data....");
			
			$http({
				method : 'POST',
				data: aviador,
				url : 'http://' + $location.host() + ':4567/cadastro'
			}).then(function successCallback(response) {				
				console.log("OK")
				
				$window.location.href = '/sucesso.html';
				
			}, function errorCallback(response) {
				if (response.status == 302) {
					$scope.jaCadastrado = true;
				} else
					console.log("Erro");
			});
		}
	};
});

app.controller('ctrlLista', function($scope, $http, $location) {
	$scope.total = 0;
	
	$http({
		method : 'GET',
		url : 'http://' + $location.host() + ':4567/lista'
	}).then(function successCallback(response) {				
		$scope.aviadores = response.data;
		
	}, function errorCallback(response) {
		console.log("Erro");
	});
	
	$scope.somaTotal = function(aviador) {
		$scope.total = $scope.total + (aviador.qtd == null ? 0 : aviador.qtd);  
	};
});