<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.id)">
        {{ item.name }}
      </template>
    </template>
  </div>
</template>

<script>
export default {
  name: "DynamicDictTag",
  props: {
    options: {
      type: Array,
      default: null,
    },
    value: [Number, String, Array],
  },
  computed: {
    values() {
      if (this.value !== null && typeof this.value !== 'undefined') {
        return Array.isArray(this.value) ? this.value : [this.value];
      } else {
        return [];
      }
    },
  },
};
</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
