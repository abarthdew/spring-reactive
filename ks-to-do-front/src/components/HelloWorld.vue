<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <hr>
    <p>
      id: <input type="text" v-model="item.id" placeholder="수정할때만 입력"/>
      todo: <input type="text" v-model="item.todo"/>
    </p>
    <button @click="save">추가 또는 수정</button>
    <hr>
    <li v-for="t in todo" :key="t.id">
      {{ t.id + ' : ' + t.todo }}
      <button @click="deleteOne(t)">삭제</button>
      <hr>
    </li>
    <button @click="deleteAll">모두 삭제</button>
    <hr>
  </div>
</template>

<script>
import axios from 'axios';
  export default {
    name: 'HelloWorld',
    data: () => {
    return {
          todo: null,
          item: {
            id: null,
            todo: null
          }
        };
    },
    props: {
      msg: String
    },
    created() {
      this.init();
    },
    methods: {
      async init() {
        await this.readAll();
        this.item = {
          id: null,
          todo: null
        }
      },
      async readAll() {
        const res = await axios.get('/api/readAll');
        this.todo = res.data;
        console.log(this.todo);
      },
      async save() {
        !this.item.id ? this.create() : this.update();
      },
      async create() {
        await axios.post('/api/create', this.item);
        await this.init();
      },
      async update() {
        await axios.put(`/api/update/${this.item.id}`, this.item);
        await this.init();
      },
      async deleteOne(todo) {
        await axios.delete(`/api/deleteOne/${todo.id}`, todo);
        await this.init();
      },
      async deleteAll() {
        await axios.delete('/api/deleteAll');
        await this.init();
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h3 {
    margin: 40px 0 0;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  a {
    color: #42b983;
  }
</style>
