package im.xgs.net.channel;
public class OrgMembers{
		private String id;
		private String parentId;
		private String name;
		private boolean leaf;
		private boolean online;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		public String getParentId() {
			return parentId;
		}
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isLeaf() {
			return leaf;
		}
		public void setLeaf(boolean leaf) {
			this.leaf = leaf;
		}
		public boolean isOnline() {
			return online;
		}
		public void setOnline(boolean online) {
			this.online = online;
		}
		
		
	}