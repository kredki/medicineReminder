package com.xyz.leki.Resource;

import java.util.ArrayList;
import java.util.List;

public class MedicineList {
    private static MedicineList instance = null;
    private static List<Medicine> medicineList;
    public static MedicineList getInstance() {
        if (instance == null) {
//            synchronized (MedicineList.getInstance()) {
            if (instance == null) {
                instance = new MedicineList();
                return instance;
            } else {
                return instance;
            }
//            }
        } else {
            return instance;
        }
    }

    private MedicineList() {
        this.medicineList = new ArrayList<>();
    }

    public static List<Medicine> getMedicineList() {
        return medicineList;
    }

    public static void setMedicineList(List<Medicine> medicineList) {
        MedicineList.medicineList = medicineList;
    }

    public static void addMedicine(Medicine med) {
        MedicineList.medicineList.add(med);
    }
}
