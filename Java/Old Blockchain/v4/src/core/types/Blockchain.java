package core.types;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.GsonBuilder;
import core.types.block.Block;

public class Blockchain {
    private ArrayList<Layer> blockchain;

    public Blockchain(){
        blockchain = new ArrayList<Layer>();
        genesis();
    }

    public void addBlock(Block block, int height){
        if(height > (blockchain.size() - 1)){
            if(height != blockchain.size()){
                System.out.println("Error: Cannot add layer " + height + " without the previous " + (height - (blockchain.size() - 1)) + " layers.");
                return;
            }else{
                blockchain.add(new Layer(height));
            }
        }
        blockchain.get(height).addBlock(block);
    }

    public void genesis(){
        blockchain.add(new Layer());
    }

    public Layer getLayer(int height){
        return blockchain.get(height);
    }

    public int getHeight(){
        return blockchain.size() - 1;
    }

    public boolean checkValid() {
        if(blockchain.get(0).grabBlock().getHeader().getBlockHash().equals("0201419acab95f0a56fd4c72798a34799299e4cb4964f0633e262b66f97e04fb")) return true;

        for(int i = 1; i < blockchain.size(); i++){
            if(!blockchain.get(i).checkValid()) {
                System.out.println("Layer " + i + " has inconsistent hash arrays.");
                return false;
            }

            if(!blockchain.get(i).grabBlock().getHeader().getHashRoot().equals(blockchain.get(i - 1).getHashRoot())){
                System.out.println("The hash arrays in layer " + i + " do not match the hashes of the previous layer.");
                return false;
            }
        }

        return true;
    }

    public void fixChain() {
        for(int i = 1; i < blockchain.size(); i++) {
            blockchain.get(i).trimInval();
            for(int j = 0; j < blockchain.get(i).getSize(); j++) {

            }
        }

    }

    public String toJson(){
        String temp = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        return temp;
    }
}
