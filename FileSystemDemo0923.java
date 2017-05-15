package myTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class FileSystemDemo0923 {

	public static void main(String[] args) {
		ViewClass viewClass = new ViewClass();
		// viewClass.toDemo();
		viewClass.mainMenu();

	}

}

/**
 * 界面显示类
 * 
 * @author silladus
 *
 */
class ViewClass {
	private Scanner input;

	private FileOpt fileOpt;
	// 记录用户输入的路径
	private String curruFilePath;

	// public void toDemo() {
	public ViewClass() {
		input = new Scanner(System.in);
		fileOpt = new FileOpt();
	}

	public void mainMenu() {
		System.out.println("==========文件操作==========");
		System.out.println("1.复制文件");
		System.out.println("2.删除文件");
		System.out.println("3.创建文件");
		System.out.println("4.创建文件夹");
		System.out.println("5.查看文件夹");
		System.out.println("6.剪切文件");
		System.out.println("7.搜索特定文件夹");
		System.out.println("请选择：");
		int opt = input.nextInt();
		switch (opt) {
		case 1:
			// 复制文件
			copyFile();
			mainMenu();
			break;
		case 2:
			// 删除文件
			System.out.println("输入要删除的文件");
			String Path = input.next();
			File file = new File(Path);
			fileDele(file);
			mainMenu();
			break;
		case 3:
			// 创建文件
			createFile();
			mainMenu();
			break;
		case 4:
			// 创建文件夹
			nMdir();
			mainMenu();
			break;
		case 5:
			// 查看文件夹
			showFile();
			mainMenu();
			break;
		case 6:
			// 剪切文件
			moveFile();
			mainMenu();
			break;
		case 7:
			// 搜索特定文件夹
			System.out.println("输入搜索路径");
			String searchPath = input.next();
			System.out.println("输入文件夹名");
			String keyStr = input.next();
			File searchResults = new File(searchPath);
			fileDele(searchResults, keyStr);
			mainMenu();
			break;
		}
	}

	// 显示文件
	public void showFile() {
		System.out.println("请输入要查看的路径");
		String path = input.next();
		curruFilePath = path;
		File[] fs = fileOpt.showFileChilden(path);
		if (fs != null) {
			System.out
					.println("--------------" + fs.length + "个文件------------");
			for (File file : fs) {
				String wString = "文件";
				if (file.isDirectory()) {
					wString = "文件夹";
				}
				System.out.println(file.getName() + "\t" + wString);
			}

			System.out.println();
			// 显示文件操作菜单
			mainMenu();
		} else {
			System.out.println("此路径不存在");
			System.out.println();
			showFile();
		}
	}

	// 复制文件
	public void copyFile() {
		System.out.println("请输入要复制的文件");
		String fName = input.next();
		System.out.println("请输入要复制的路径");
		String targetPath = input.next();
		String source = curruFilePath + "\\" + fName;
		String target = targetPath + "\\" + fName;
		boolean r = fileOpt.copy(source, target);
		if (r) {
			System.out.println("复制完成");
		} else {
			System.out.println("复制失败");
		}
	}

	// 删除文件
	public void fileDele(File file) {
		if (file.isFile()) {
			boolean fd = file.delete();
			if (fd) {
				System.out.println(file.getPath() + " 删除完毕");
			}
		} else {
			// 如果文件夹内容不为空则执行1，空则直接执行2
			// 1
			File[] fs = file.listFiles();
			for (File f : fs) {
				if (f.isFile()) {
					boolean fd2 = f.delete();
					if (fd2) {
						System.out.println(f.getPath() + " 删除完毕");
					}
				} else {
					fileDele(f);
				}
			}
			// 2
			boolean md = file.delete();
			if (md) {
				System.out.println(file.getPath() + "\t文件夹删除完毕");
			}
		}
	}

	// 删除特定文件夹
	public void fileDele(File file, String name) {
		File[] fs = file.listFiles();
		for (File f : fs) {
			if (f.getName().equals(name)) {
				fileDele(f);
			} else {
				if (f.isDirectory()) {
					fileDele(f, name);
				}
			}
		}
	}

	// 创建文件
	public void createFile() {
		System.out.println("请输入文件名");
		String path = input.next();
		if (path.contains(".")) {
			// 创建文件
			boolean isCreate = fileOpt.createFile(path);
			if (isCreate) {
				System.out.println("文件创建完成");
			} else {
				System.err.println("文件创建失败");
			}
		} else {
			System.err.println("请输入拓展名");
		}
	}

	// 创建文件夹
	public void nMdir() {
		System.out.println("请输入文件夹路径");
		String path = input.next();
		boolean isDir = fileOpt.nMdirs(path);
		if (isDir) {
			System.out.println("文件夹创建完成");
		} else {
			System.err.println("文件夹创建失败");
		}
	}

	// 剪切文件
	public void moveFile() {
		System.out.println("请选择要移动的文件");
		String fromString = input.next();
		System.out.println("移动到：");
		String toString = input.next();
		boolean isMove = fileOpt.moveFile(fromString, toString);
		if (isMove) {
			System.out.println("文件移动完成");
		} else {
			System.out.println("文件移动失败");
		}
	}
}

/**
 * 文件操作类
 * 
 * @author silladus
 *
 */
class FileOpt {
	// 复制文件
	public boolean copy(String source, String target) {
		try {
			FileInputStream in = new FileInputStream(source);
			FileOutputStream out = new FileOutputStream(target);
			int content = 0;
			while ((content = in.read()) != -1) {
				out.write(content);
			}
			out.flush();
			out.close();
			in.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// 显示文件
	public File[] showFileChilden(String path) {
		File f = new File(path);
		if (f != null && f.isDirectory()) {
			File[] fs = f.listFiles();
			return fs;
		}
		return null;
	}

	// 创建文件
	public boolean createFile(String path) {
		File nFile = new File(path);
		// 创建文件
		boolean isCreate = false;
		try {
			isCreate = nFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isCreate) {
			// System.out.println(nFile.getName() + " 创建完成");
			return true;
		} else {
			// System.out.println("文件创建失败");
			return false;
		}
	}

	// 创建文件夹
	public boolean nMdirs(String path) {
		// 创建文件夹
		File nDir = new File(path);
		boolean isDir = nDir.mkdirs();
		if (isDir) {
			// System.out.println(nDir.getName() + " 创建完成");
			return true;
		} else {
			// System.out.println("文件夹创建失败");
			return false;
		}
	}

	// 剪切文件
	public boolean moveFile(String from, String to) {
		File sFile = new File(from);
		File tFile = new File(to);
		boolean isRename = sFile.renameTo(tFile);
		if (isRename) {
			return true;
		} else {
			return false;
		}
	}
}