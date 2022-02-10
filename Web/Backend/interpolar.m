% % function x = interpolar(n1,n2,n3)
% %   x = n1+n2+n3;
% % end

% datasource = "MySQL";
% username = "root";
% password = "";
% conn = database(datasource,username,password);
% 
% 
% % function [latitudFinal, longitudFinal, valorAzufreFinal] = interpolar(latitud, longitud, valorAzufre)
% % x1=min(longitud):0.001:max(longitud);y1=min(latitud):0.001:max(latitud);
% % [longitudFinal,latitudFinal]=meshgrid(x1,y1);
% % valorAzufreFinal=griddata(longitud,latitud,valorAzufre,longitudFinal,latitudFinal,'v4');
% % % pcolor(longitudFinal,latitudFinal,valorAzufreFinal), shading interp
% % % filename = fullfile('C:\xampp\htdocs\Web\Frontend', 'mapadecalor.png');
% % % set(gca,'XTick',[], 'YTick', [])
% % % set(gca,'LooseInset',get(gca,'TightInset'))
% % % saveas(gcf, filename)
% % %out = X1, Y1, valor;
% % end
% % %out = X1, Y1, valor;
% % 
% % %csvwrite('result.csv', out);
% 
% % function variableReturn = interpolar(latitud, longitud, valorAzufre)
% %     x1=-0.22955:0.001:-0.142;y1=38.95872:0.001:39.0268;
% %     [X1,Y1]=meshgrid(x1,y1);
% %      variableReturn=griddata(longitud,latitud,valorAzufre,X1,Y1);
% % %    variableReturn=interp2(longitud,latitud,valorAzufre,X1,Y1,'spline');
% %     
% % end
% 
% if isconnection(conn)
%  qry = sprintf('INSERT INTO lecturas(top,bottom,left,right) VALUES(%.3f,%.3f,%.3f,%.3f);',distAa,distBa,distCa,distDa);
%     display(qry);
%     fetch(exec(conn, qry));
%  else
%     display('MySql Connection Error');
% end
% 
% function x = interpolar()
%     x = 100;
%     disp(x);
% end
% 
% close(conn)



function [X1, Y1, valorAzufreFinal] = interpolar(latitud, longitud, valorAzufre)
x1=-0.22955:0.001:-0.142;y1=38.95872:0.001:39.0268;
[X1,Y1]=meshgrid(x1,y1);
disp(latitud);
disp(longitud);
disp(valorAzufre);
valorAzufreFinal=griddata(longitud,latitud,valorAzufre,X1,Y1,'v4');
% pcolor(longitudFinal,latitudFinal,valorAzufreFinal), shading interp
% filename = fullfile('C:\xampp\htdocs\Web\Frontend', 'mapadecalor.png');
% set(gca,'XTick',[], 'YTick', [])
% set(gca,'LooseInset',get(gca,'TightInset'))
% saveas(gcf, filename)
%out = X1, Y1, valor;
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
   %recorrer la matriz
t = datetime("now");
v = datevec(t);
fechaDia = datetime(v(:,1:3));
fechaDiaString = datestr(fechaDia)
fechaHora = duration(v(:,4:end));
fechaHoraString = datestr(fechaHora)
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

function [X1, Y1, valorAzufre] = cogerBaseDeDatos(dia)
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
     selectquery = 'SELECT ubicacion, valor FROM Lecturas WHERE dia = dia';

data = select(conn,selectquery)

else
    display('MySql Connection Error');
 end
end
