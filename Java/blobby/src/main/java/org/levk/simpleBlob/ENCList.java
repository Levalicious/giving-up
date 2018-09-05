package org.levk.simpleBlob;

import java.util.ArrayList;
import java.util.List;

public class ENCList extends ArrayList<ENCItem> {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ENCList)) {
            return false;
        }

        if (((ENCList) o).size() != this.size()) {
            return false;
        }

        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).equals(((ENCList) o).get(i))) {
                System.out.println("Byte array " + i + " is different.");
                return false;
            }
        }

        return true;
    }
}
