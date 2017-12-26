package com.kcb.encload.util.loader.enc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.kcb.encload.util.Utils;

/**
 * 암호화된 자원을 메모리에 적재한다.
 * 
 * @author O117012
 */
public class EncryptedClassLoader extends ClassLoader {
    /**
     * 클래스 저장소 저장소
     */
	private final HashMap<String, byte[]> classes = new HashMap<String, byte[]>();

    /**
     * 리소스 저장소
     */
	private final HashMap<String, byte[]> others = new HashMap<String, byte[]>();
	/**
	 * 암호화리소스 여부
	 */
	private final boolean encryptResources;
	
	/**
	 * 생성자이다.
	 * 
	 * @param parent 부모 클래스로더
	 * @param stream jarInputStream
	 * @param encryptResources 암호화 리소스 여부
	 */
	public EncryptedClassLoader(ClassLoader parent, JarInputStream stream, boolean encryptResources) {
		super(parent);
		this.loadResources(stream);
		this.encryptResources = encryptResources;
	}

	/**
	 * 암호화 여부에 따라 캐싱된 리소스의 스트림을 반환한다.
	 */
	@Override
	public InputStream getResourceAsStream(String name) {
		if (encryptResources) {
			byte[] buffer = others.get(name);
			if (buffer != null) {
				return new ByteArrayInputStream(buffer);
			}
		}

		return super.getResourceAsStream(name);
	}

	@Override
	public URL getResource(String name) {
		if (encryptResources) {
			throw null;
        } else {
			return super.getResource(name);
		}
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		if (encryptResources) {
			throw new IOException("Cant get URL from resource in memory");
		} else {
			return super.findResources(name);
		}
	}

	@Override
	public int hashCode() {
		return getParent().hashCode();
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] data = getClassData(name);
		
		if (data != null) {
			return defineClass(name, data, 0, data.length);
		} else {
			throw new ClassNotFoundException(name);
		}
	}

	/**
	 * 전달받은 JarInputStream에서 리소스와 class를 메모리에 캐싱한다.
	 * 
	 * @param stream jarInputStream
	 */
	public void loadResources(JarInputStream stream) {
		byte[] buffer = new byte[1024];

		int count;

		try {
			JarEntry entry = null;
			while ((entry = stream.getNextJarEntry()) != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();

				while ((count = stream.read(buffer)) != -1) {
					out.write(buffer, 0, count);
				}

				out.close();

				byte[] array = out.toByteArray();
				
				if (entry.getName().toLowerCase().endsWith(".class")) {
					classes.put(Utils.getClassName(entry.getName()), array);
				} else if (encryptResources) {
					others.put(entry.getName(), array);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EncryptedClassLoader) {
			return ((EncryptedClassLoader) o).getParent() == getParent();
		}
		return false;
	}

	public byte[] getClassData(String name) {
		byte[] b = classes.get(name);
		classes.remove(name);
		return b;
	}

}
