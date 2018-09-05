package core.types;

import core.types.block.Block;

import java.lang.reflect.Array;
import java.util.*;

import static crypto.Hash.getStringMerkleRoot;

public class Layer {
    private long height;
    private ArrayList<Block> layer;

    Layer() {
        layer = new ArrayList<Block>();
    }

    Layer(long height) {
        layer = new ArrayList<Block>();
        this.height = height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void addBlock(Block block){
        layer.add(block);
        Collections.sort(layer);
    }

    public ArrayList<Block> getBlocks() {
        return this.layer;
    }

    public Block getBlock(String prefix) {
        try {
            for(int i = 0; i < layer.size(); i++) {
                if(layer.get(i).getHeader().getPrefix().equals(prefix)) {
                    return layer.get(i);
                }
            }
            System.out.println("No block exists in this layer with the prefix " + prefix);
            throw new Exception();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(){
        Collections.sort(layer);
        String temp = "";
        for(int i = 0; i < layer.size(); i++){
            temp = (temp + "\n\n" + layer.get(i).toString());
        }
        return temp;
    }

    public String getHashRoot(){
        ArrayList<String> temp = new ArrayList<String>();
        for(int i = 0; i < layer.size(); i++){
            temp.add(layer.get(i).getHeader().getBlockHash());
        }
        return getStringMerkleRoot(temp);
    }

    public boolean checkValid() {
        String temp = layer.get(0).getHeader().getHashRoot();
        for(long i = 0; i < layer.size(); i++) {
            if(!temp.equals(layer.get((int)i).getHeader().getHashRoot())) {
                return false;
            }
        }
        return true;
    }

    public Block grabBlock() {
        return layer.get(0);
    }

    public long getSize() {
        return (long)layer.size();
    }

    public void trimInval() {
        Collections.sort(layer);
        HashMap<String, Long> hashList = new HashMap<String, Long>();
        if(!checkValid()) {
            long oldSize = layer.size();
            long removed = 0;
            for(long i = 0; i < layer.size(); i++) {
                if(!layer.get((int)i).validate()) {
                    layer.remove(i);
                    removed++;
                }
            }

            System.out.println("Removed " + removed + " blocks from layer containing " + oldSize + " blocks.");
        }else {
            System.out.println("No inconsistencies in layer hashes; no trimming necessary.");
        }
    }
}
