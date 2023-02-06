package org.example;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Main extends Application {


    public static boolean cross(float x1,float y1, float x2, float y2,float x3, float y3, float x4, float y4){
        List<Float> point = new ArrayList<>();
        float n=0;
        if(y2-y1!=0){
            float q=(x2-x1)/(y1-y2);
            float sn = (x3-x4)+((y3-y4)*q);
            if(sn==0){
                return false;
            }
            float fn=(x3-x1)+((y3-y1)*q);
            n=fn/sn;
        }else{
            if((y3-y4)==0){
                return false;
            }
            n=(y3-y1)/(y3-y4);
        }
        point.add(x3+(x4-x3)*n);
        point.add(y3+(y4-y3)*n);

        if(x1==x2){
            if((point.get(1)>=y1&&point.get(1)<=y2)||(point.get(1)<=y1&&point.get(1)>=y2)){
                return true;
            }else {
                return false;
            }
        }else {
            float a = (y1-y2)/x1-x2;
            float b =((y1+y2)-a*(x1+x2))/2;
            if((point.get(1)==a*point.get(0)+b && point.get(0)>x1  && point.get(0)<x2) || (point.get(1)==a*point.get(0)+b && x2>x1 && point.get(0)<x1)){
                return true;
            }else {
                return false;
            }
        }
    }

    public static double module(float x, float y){
        return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }

    public static double scalar(float x1, float y1, float x2, float y2){
        return (x1*x2)+(y1*y2);
    }

    public static double cosine(float x1, float y1, float x2, float y2,float x3, float y3, float x4, float y4){

        return scalar(x2-x1,y2-y1,x4-x3,y4-y3)/(module(x2-x1,y2-y1)*module(x4-x3,y4-y3));
    }


    public static List<List<List<Integer>>> algorithm() throws Exception{
        int countOfCrosses = 0;
        List<Integer> point = new ArrayList<>();
        List<List<Integer>> coordinates = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введіть кількість точок: ");
        int amount = scanner.nextInt();
        if (amount < 2){
            System.out.println("Має бути від двох точок до будь-якої кількості!");
            throw new Exception();
        }

        System.out.println("Координати мають бути в межах [-9; 9]");
        for (int i=0;i<amount;i++){
            System.out.println("Вкажіть координати точки "+(i+1)+" по X:");
            int x= scanner.nextInt();
            if (x < -9 || x > 9){
                System.out.println("Зверніть увагу! Координати мають бути в межах [-9; 9]");
                throw new Exception();
            }
            System.out.println("Вкажіть координати точки "+(i+1)+" по Y:");
            int y = scanner.nextInt();
            if (y < -9 || y > 9){
                System.out.println("Зверніть увагу! Координати мають бути в межах [-9; 9]");
                throw new Exception();
            }
            List<Integer> xy = new ArrayList<>();
            xy.add(x);
            xy.add(y);
            coordinates.add(xy);
        }

        System.out.println("Вкажіть точку на площині: ");
        System.out.println("X: ");
        int x1 = scanner.nextInt();
        System.out.println("Y: ");
        int y1 = scanner.nextInt();
        point.add(x1);
        point.add(y1);
        List<Integer> line = new ArrayList<>();
        line.add(1000);
        line.add(y1);

        for (int i=0;i<amount;i++){
            List<Integer> second;
            if(i==amount-1){
                second=coordinates.get(0);
            }else {
                second = coordinates.get(i+1);
            }
            List<Integer> first = coordinates.get(i);
            if(cross(first.get(0),first.get(1),second.get(0),second.get(1),point.get(0),point.get(1),line.get(0),line.get(1))){
                countOfCrosses++;
            }
        }
        if (countOfCrosses%2 == 0){
            System.out.println("Точка знаходиться зовні багатокутника.");
        }else {
            System.out.println("Точка знаходиться всередині багатокутника.");
        }

        List<List<Integer>> toDelete = new ArrayList<>();
        List<List<Integer>> SCS;

        SCS=coordinates.stream().sorted(Comparator.comparing(s-> s.get(0))).collect(Collectors.toList());
        List<Integer> first = SCS.get(0);
        SCS = SCS.stream().sorted(Comparator.comparing(s-> s.get(1))).collect(Collectors.toList());
        SCS.remove(first);
        SCS.add(0,first);

        List<Integer> current=SCS.get(0);
        int i=0;
        while (true){

            List<Integer> next= SCS.get(i+1);

            double cos1= cosine(current.get(0),current.get(1),next.get(0),next.get(1),next.get(0),next.get(1), SCS.get(i+2).get(0),SCS.get(i+2).get(1));
            double cos2= cosine(current.get(0),current.get(1),next.get(0),current.get(1),next.get(0),next.get(1),SCS.get(i+2).get(0),SCS.get(i+3).get(1));
            if(cos2>cos1&&!SCS.get(i+2).equals(SCS.get(2))){
                toDelete.add(SCS.get(i+2));
            }
            i++;
            if(i==coordinates.size()-3){
                break;
            }else {
                current = SCS.get(i+2);
            }

        }
        SCS.removeAll(toDelete);

        List<List<Integer>> SCS2 = new ArrayList<>();
        List<List<Integer>> coordinatesSorted=coordinates.stream().sorted(Comparator.comparing(s-> s.get(0))).collect(Collectors.toList());
        SCS2.add(coordinatesSorted.get(0));

        while(true){
            if(i==coordinates.size()-1){
                break;
            }
            List<Integer> dot = coordinatesSorted.get(i);
            if(dot.get(1)>=coordinatesSorted.get(i+1).get(1)){
                SCS2.add(coordinatesSorted.get(i+1));
            } else if (dot.get(0) <= coordinatesSorted.get(i + 1).get(0)) {
                SCS2.add(coordinatesSorted.get(i+1));
            }
            i++;
        }
        coordinates.add(point);
        List<List<List<Integer>>>  list = new ArrayList<>();
        list.add(coordinates);
        list.add(SCS);
        return list;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Path path = new Path();
        Path path1 = new Path();
        Circle point = new Circle();

        List<Node> nodes = new ArrayList<>();
        List<List<List<Integer>>> listsCoordinates = algorithm();

        List<List<Integer>> coordinates = listsCoordinates.get(0);

        MoveTo moveTo = new MoveTo(500+(coordinates.get(0).get(0)*50),1000-(500+(coordinates.get(0).get(1)*50)));
        path.getElements().add(moveTo);
        LineTo line;

        for (int i=1;i<coordinates.size()-1;i++){

            Circle circle = new Circle();
            circle.setCenterX(500+(coordinates.get(i).get(0) * 50));
            circle.setCenterY(1000-(500+(coordinates.get(i).get(1) * 50)));
            circle.setRadius(5);
            circle.setFill(Color.BLUE);
            nodes.add(circle);
            line = new LineTo(500+(coordinates.get(i).get(0) * 50), 1000-(500+(coordinates.get(i).get(1) * 50)));
            path.getElements().add(line);
        }
        line = new LineTo(500+(coordinates.get(0).get(0)*50),1000-(500+(coordinates.get(0).get(1)*50)));
        path.getElements().add(line);
        Circle circle = new Circle();
        circle.setCenterX(500+(coordinates.get(0).get(0) * 50));
        circle.setCenterY(1000-(500+(coordinates.get(0).get(1) * 50)));
        circle.setRadius(5);
        circle.setFill(Color.BLUE);
        nodes.add(circle);

        List<List<Integer>> SCSCoordinates = listsCoordinates.get(1);
        MoveTo moveTo1 = new MoveTo(500+(SCSCoordinates.get(0).get(0)*50),1000-(500+(SCSCoordinates.get(0).get(1)*50)));
        path1.getElements().add(moveTo1);
        LineTo SCSLine;

        for (int i=1; i< SCSCoordinates.size();i++){
            Circle circle1 = new Circle();
            circle1.setCenterX(500+(SCSCoordinates.get(i).get(0)*50));
            circle1.setCenterY(1000-(500+SCSCoordinates.get(i).get(1)*50));
            circle1.setRadius(5);
            circle1.setFill(Color.BLUE);
            nodes.add(circle1);
            SCSLine = new LineTo(500+(SCSCoordinates.get(i).get(0)*50),1000-(500+SCSCoordinates.get(i).get(1)*50));
            path1.getElements().add(SCSLine);
        }
        SCSLine = new LineTo(500+(SCSCoordinates.get(0).get(0)*50),1000-(500+(SCSCoordinates.get(0).get(1)*50)));
        path1.getElements().add(SCSLine);
        Circle circle1 = new Circle();
        circle1.setCenterX(500+(SCSCoordinates.get(0).get(0)*50));
        circle1.setCenterY(1000-(500+(SCSCoordinates.get(0).get(1)*50)));
        circle1.setRadius(5);
        circle1.setFill(Color.BLUE);
        nodes.add(circle1);

        point.setCenterX(500+(coordinates.get(coordinates.size()-1).get(0)*50));
        point.setCenterY(1000-(500+(coordinates.get(coordinates.size()-1).get(1)*50)));
        point.setRadius(5);
        point.setFill(Color.RED);

        Path path2 = new Path();
        Path path3 = new Path();
        MoveTo axisX = new MoveTo(0,500);
        MoveTo axisY = new MoveTo(500,0);
        LineTo lineX = new LineTo(1000, 500);
        LineTo lineY = new LineTo(500, 1000);
        path2.getElements().add(axisX);
        path3.getElements().add(axisY);
        path2.getElements().add(lineX);
        path3.getElements().add(lineY);

        nodes.add(point);
        nodes.add(path);
        nodes.add(path1);
        nodes.add(path2);
        nodes.add(path3);
        Group root = new Group(nodes);
        Scene scene = new Scene(root,1000,1000);
        stage.setTitle("Багатокутник");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println("****************************");
        System.out.println(" ");

        try {
            launch(args);
        }catch (Exception exception){
            System.out.println("Error!");
        }

        System.out.println(" ");
        System.out.println("****************************");
    }
}
