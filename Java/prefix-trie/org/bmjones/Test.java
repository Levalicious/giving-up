package org.bmjones;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.bmjones.util.BlockTree;

public class Test {
	
	public static void main(String[] args) throws IOException {
		List<String> items = Files.readAllLines(Paths.get("in.txt"));
		
		long start = System.currentTimeMillis();
		
		BlockTree<String> tree = new BlockTree<>(50);
		tree.addAll(items);
				
		Files.write(Paths.get("out.txt"), tree.toString().getBytes());
		
		long end = System.currentTimeMillis();
		
		System.out.println("Done. Took " + (end - start) + "ms");
	}

}
