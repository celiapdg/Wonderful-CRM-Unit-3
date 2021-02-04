package com.ironhack.CRMunit3.utils;

import java.util.List;

public class Math {

    public static double median(List<Object[]> orderedList){
        if (orderedList.size()>0){
            // Even-sized lists
            if (orderedList.size()%2==0){
                double first = (double) orderedList.get((orderedList.size()/2)-1)[0];
                double second = (double) orderedList.get(orderedList.size()/2)[0];
                return (first+second)/2.0;
            // Odd-sized lists
            }else{
                Integer index = (orderedList.size()-1)/2;
                return (double) orderedList.get(index)[0];
            }
        }else{
            throw new IllegalArgumentException("There are no elements to calculate the median");
        }

    }
}
