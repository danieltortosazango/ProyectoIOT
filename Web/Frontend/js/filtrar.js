var btnAbrirFiltrar = document.getElementById('btn-abrir-filtrar'),
	filtrar = document.getElementById('filtrar');
	btnCerrarFiltrar = document.getElementById('btn-cerrar-filtrar');

btnAbrirFiltrar.addEventListener('click', function(){
	filtrar.classList.add('active');
});

btnCerrarFiltrar.addEventListener('click', function(e){
	e.preventDefault();
	filtrar.classList.remove('active');
});