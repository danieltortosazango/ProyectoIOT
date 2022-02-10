function data = cogerBaseDeDatos(dia)
javaaddpath('C:\Users\Daniel\Downloads\mysql-connector-java-8.0.12.jar')
    %# connection parameteres
    host = 'localhost'; %MySQL hostname
    user = 'root'; %MySQL username
    password = '';%MySQL password
    dbName = 'lecturas'; %MySQL database name
    %# JDBC parameters
    jdbcString = sprintf('jdbc:mysql://%s/%s', host, dbName);
    jdbcDriver = 'com.mysql.jdbc.Driver';
 conn = database(dbName, user , password); 
 if isopen(conn)
     %selectquery = 'SELECT ubicacion, valor FROM Lecturas WHERE dia = "11/01/2021"';
     %tempText = "Temperature is " + c + "C"
     selectquery = 'SELECT ubicacion, valor FROM Lecturas WHERE dia = ';
     stringComilla = '"';
     stringDia = strcat(stringComilla, dia, stringComilla);
     s = strcat(selectquery,stringDia);
     %disp(s)
    data = select(conn,s);
    %disp(data);
    
    dataArray = table2cell(data);
    %disp(dataArray);
    %dataArrayMat = cell2mat(dataArray);
    [f,c] = size(dataArray);
    celdaUbicaciones={};
    celdaValores={};
        for j=1:c
          for i=1:f
               if(j == 1)
%                   arrayUbicaciones(i)=dataArray(i,j);
                celdaUbicaciones(i)=dataArray(i,j);

               end
               if(j == 2)
%                   arrayValores(i)=dataArray(i,j);
                celdaValores(i)=dataArray(i,j);
              end
            %disp(dataArray(i,j));
          end
        end
        
        dataArrayValores = cell2mat(celdaValores);
        dataArrayStringUbicacion = string(celdaUbicaciones);
        dataArraySpliteado = split(dataArrayStringUbicacion,"/");
        
        dataArrayStringUbicacionLatitud = dataArraySpliteado(:,:,1);
        dataArrayStringUbicacionLongitud = dataArraySpliteado(:,:,2);
        
        dataArrayLatitudDouble = str2double( dataArrayStringUbicacionLatitud );
        dataArrayLongitudDouble = str2double( dataArrayStringUbicacionLongitud );
        
        interpolar(dataArrayLatitudDouble, dataArrayLongitudDouble, dataArrayValores);

else
    display('MySql Connection Error');
 end
end


function [X1, Y1, valorAzufreFinal] = interpolar(latitud, longitud, valorAzufre)
x1=-0.22955:0.001:-0.142;y1=38.95872:0.001:39.0268;
[X1,Y1]=meshgrid(x1,y1);
disp(latitud);
disp(longitud);
disp(valorAzufre);
longitudFuncion = [];
latitudFuncion = [];
valorAzufreFuncion = [];

[f,c] = size(latitud);
      for j=1:c
          for i=1:f
          longitudFuncion(j) = longitud(j);
          latitudFuncion(j) = latitud(j);
          valorAzufreFuncion(j) = valorAzufre(j);
          end
      end
    

valorAzufreFinal=griddata(longitudFuncion,latitudFuncion,valorAzufreFuncion,X1,Y1,'v4');
% pcolor(longitudFinal,latitudFinal,valorAzufreFinal), shading interp
% filename = fullfile('C:\xampp\htdocs\Web\Frontend', 'mapadecalor.png');
% set(gca,'XTick',[], 'YTick', [])
% set(gca,'LooseInset',get(gca,'TightInset'))
% saveas(gcf, filename)
%out = X1, Y1, valor;
    javaaddpath('./mysql-connector-java-8.0.12.jar')
    %# connection parameteres
    host = 'localhost'; %MySQL hostname
    user = 'root'; %MySQL username
    password = '';%MySQL password
    dbName = 'lecturas'; %MySQL database name
    %# JDBC parameters
    jdbcString = sprintf('jdbc:mysql://%s/%s', host, dbName);
    jdbcDriver = 'com.mysql.jdbc.Driver';
 conn = database(dbName, user , password); 
 if isopen(conn)
   %recorrer la matriz
t = datetime("now");
v = datevec(t);
fechaDia = datetime(v(:,1:3));
fechaDiaString = datestr(fechaDia);
fechaHora = duration(v(:,4:end));
fechaHoraString = datestr(fechaHora);
%disp(fechaDia)
%disp(fechaHora)

[f,c] = size(X1);
     for j=1:c
         for i=1:f
            %disp(tiempo)
            %disp(X1(i,j))%longitud
            %disp(Y1(i,j))%latitud
            %disp(valorAzufre(i,j))%valor interpolado
             %qry = sprintf('INSERT INTO interpolacion(dia,hora,latitud,longitud,valor) VALUES(%.3f,%.3f,%.3f,%.3f,%.3f);',"hora","19",5,6,7);
              fastinsert(conn, 'interpolacion', {'dia','hora','latitud','longitud','valor'}, {fechaDiaString,fechaHoraString,Y1(i,j),X1(i,j),valorAzufreFinal(i,j)});
        %display(qry);
        %fetch(exec(conn, qry));
         end
     end
 else
    display('MySql Connection Error');
 end

end
